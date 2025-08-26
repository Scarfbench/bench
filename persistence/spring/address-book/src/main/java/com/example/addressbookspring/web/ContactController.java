package com.example.addressbookspring.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import jakarta.validation.Valid;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import com.example.addressbookspring.entity.Contact;
import com.example.addressbookspring.service.ContactService;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION) 
@RequestMapping("/contacts")
public class ContactController implements Serializable {

    private static final long serialVersionUID = -8163374738411860012L;

    private final ContactService ejbFacade;

    private Contact current;
    private List<Contact> items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ContactController(ContactService ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public Contact getSelected() {
        if (current == null) {
            current = new Contact();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ContactService getFacade() {
        return ejbFacade;
    }

    public static abstract class PaginationHelper {
        private final int pageSize;
        private int page;

        protected PaginationHelper(int pageSize) { this.pageSize = pageSize; }

        public abstract int getItemsCount();

        public abstract List<Contact> createPageData();

        public int getPageFirstItem() {
        return page*pageSize;
        }

        public int getPageLastItem() {
            int i = getPageFirstItem() + pageSize -1;
            int count = getItemsCount() - 1;
            if (i > count) {
                i = count;
            }
            if (i < 0) {
                i = 0;
            }
            return i;
        }

        public boolean isHasNextPage() {
            return (page+1)*pageSize+1 <= getItemsCount();
        }

        public void nextPage() {
            if (isHasNextPage()) {
                page++;
            }
        }

        public boolean isHasPreviousPage() {
            return page > 0;
        }

        public void previousPage() {
            if (isHasPreviousPage()) {
                page--;
            }
        }

        public int getPageSize() {
            return pageSize;
        }

    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public List<Contact> createPageData() {
                    int first = getPageFirstItem();
                    int lastExclusive = getPageFirstItem() + getPageSize();
                    return getFacade().findRange(new int[]{ first, lastExclusive });
                }
            };
        }
        return pagination;
    }

    @GetMapping({"", "/list"})
    public String prepareList(Model model) {
        recreateModel();
        model.addAttribute("items", getItems());
        model.addAttribute("pagination", getPagination());
        return "List";
    }

    @GetMapping("/view/{id}")
    public String prepareView(@PathVariable Long id, Model model) {
        current = getFacade().find(id);
        if (current == null) {
            return prepareList(model);
        }
        List<Contact> page = getItems();
        selectedItemIndex = getPagination().getPageFirstItem()
                + Math.max(0, page.indexOf(current));
        model.addAttribute("contact", current);
        return "View";
    }

    @GetMapping("/create")
    public String prepareCreate(Model model) {
        current = new Contact();
        selectedItemIndex = -1;
        model.addAttribute("contact", current);
        return "Create";
    }

    @PostMapping
    public String create(@ModelAttribute("contact") @Valid Contact contact,
                     BindingResult br,
                     Model model) {
        try {
            current = contact;
            getFacade().create(current);
            model.addAttribute("success",
                ResourceBundle.getBundle("Bundle").getString("ContactCreated"));
            return prepareCreate(model);
        } catch (Exception e) {
            model.addAttribute("error",
                ResourceBundle.getBundle("Bundle").getString("PersistenceErrorOccured"));
            return "Create";
        }
    }

    @GetMapping("/edit/{id}")
    public String prepareEdit(@PathVariable Long id, Model model) {
        current = getFacade().find(id);
        if (current == null) return prepareList(model);
        List<Contact> page = getItems();
        selectedItemIndex = getPagination().getPageFirstItem()
                + Math.max(0, page.indexOf(current));
        model.addAttribute("contact", current);
        return "Edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                     @ModelAttribute("contact") @Valid Contact contact,
                     BindingResult br,
                     Model model) {
        try {
            current = contact;
            current.setId(id);
            getFacade().edit(current);
            model.addAttribute("contact", current);
            model.addAttribute("success",
                ResourceBundle.getBundle("Bundle").getString("ContactUpdated"));
            return "View";
        } catch (Exception e) {
            model.addAttribute("error",
                ResourceBundle.getBundle("Bundle").getString("PersistenceErrorOccured"));
            return "Edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String destroy(@PathVariable Long id, Model model) {
        current = getFacade().find(id);
        if (current != null) performDestroy(model);
        recreateModel();
        return "List";
    }

    @PostMapping("/delete-and-view/{id}")
    public String destroyAndView(@PathVariable Long id, Model model) {
        current = getFacade().find(id);
        if (current != null) performDestroy(model);
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{ selectedItemIndex, selectedItemIndex + 1 }).get(0);
            model.addAttribute("contact", current);
            return "View";
        } else {
            return "List";
        }
    }

    private void performDestroy(Model model) {
        try {
            getFacade().remove(current);
            model.addAttribute("success",
                ResourceBundle.getBundle("Bundle").getString("ContactDeleted"));
        } catch (Exception e) {
            model.addAttribute("error",
                ResourceBundle.getBundle("Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            selectedItemIndex = count - 1;
            if (getPagination().getPageFirstItem() >= count) {
                getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            List<Contact> one = getFacade().findRange(new int[]{ selectedItemIndex, selectedItemIndex + 1 });
            if (!one.isEmpty()) current = one.get(0);
        } else {
            current = null;
        }
    }

    public List<Contact> getItems() {
        if (items == null) {
            items = new ArrayList<>(getPagination().createPageData());
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    @GetMapping("/next")
    public String next(Model model) {
        getPagination().nextPage();
        recreateModel();
        return prepareList(model);
    }

    @GetMapping("/previous")
    public String previous(Model model) {
        getPagination().previousPage();
        recreateModel();
        return prepareList(model);
    }
    
    @ModelAttribute("itemsAvailableSelectMany")
    public List<Contact> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    @ModelAttribute("itemsAvailableSelectOne")
    public List<Contact> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
}
