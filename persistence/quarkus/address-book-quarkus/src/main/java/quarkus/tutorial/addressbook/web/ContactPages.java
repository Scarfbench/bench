package jakarta.tutorial.addressbook.web;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Collections;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;

import jakarta.tutorial.addressbook.entity.Contact;
import jakarta.tutorial.addressbook.repository.ContactRepository;

@Path("/contacts")
@Produces(MediaType.TEXT_HTML)
public class ContactPages {

  @Inject ContactRepository contactRepo;
  @Inject Validator validator;

  @Location("contacts/List.html") Template listT;
  @Location("contacts/View.html") Template viewT;
  @Location("contacts/Form.html") Template formT;

  /* --- helpers --- */
  private ResourceBundle bundle() {
    return ResourceBundle.getBundle("Bundle");
  }
  private Map<String,String> i18n() {
    var b = bundle();
    Map<String,String> m = new HashMap<>();
    for (Enumeration<String> e = b.getKeys(); e.hasMoreElements();) {
      String k = e.nextElement();
      m.put(k, b.getString(k));
    }
    return m;
  }
  private String enc(String s) { return URLEncoder.encode(s, StandardCharsets.UTF_8); }

  /* --- pages --- */

  @GET
  public String list(@QueryParam("page") @DefaultValue("0") int page,
                     @QueryParam("size") @DefaultValue("10") int size,
                     @QueryParam("success") String success,
                     @QueryParam("error")   String error) {

    int first = Math.max(0, page) * Math.max(1, size);
    List<Contact> items = contactRepo.findRange(new int[]{ first, first + size });
    int total = contactRepo.count();

    int start = total == 0 ? 0 : first + 1;
    int end   = Math.min(first + items.size(), total);
    boolean hasPrev = page > 0;
    boolean hasNext = (page + 1) * size < total;
    int prevPage = Math.max(0, page - 1);
    int nextPage = page + 1;

    return listT
        .data("items", items)
        .data("page", page)
        .data("size", size)
        .data("total", total)
        .data("start", start)
        .data("end", end)
        .data("hasPrev", hasPrev)
        .data("hasNext", hasNext)
        .data("prevPage", prevPage)
        .data("nextPage", nextPage)
        .data("bundle", bundle())
        .data("i18n", i18n())
        .data("success", success)
        .data("error", error)
        .render();
  }

  @GET @Path("/new")
  public String createForm(@QueryParam("success") String success,
                         @QueryParam("error")   String error) {
  return formT
      .data("title", bundle().getString("CreateContactTitle"))
      .data("action", "/contacts")
      .data("contact", new Contact())
      .data("dateValue", "")
      .data("bundle", bundle())
      .data("i18n", i18n())
      .data("errors", Collections.emptyMap())
      .data("errorList", Collections.emptyList())
      .data("success", success)
      .data("error", error)
      .render();
}


  @GET @Path("{id}")
  public String view(@PathParam("id") long id,
                     @QueryParam("success") String success,
                     @QueryParam("error")   String error) {
    Contact c = contactRepo.find(id);
    if (c == null) throw new NotFoundException();

    String birthdayText = (c.getBirthday() == null)
        ? ""
        : new SimpleDateFormat("MM/dd/yyyy").format(c.getBirthday());

    return viewT
        .data("c", c)
        .data("birthdayText", birthdayText)
        .data("bundle", bundle())
        .data("i18n", i18n())
        .data("success", success)
        .data("error", error)
        .render();
  }

  @GET @Path("{id}/edit")
  public String editForm(@PathParam("id") long id,
                       @QueryParam("success") String success,
                       @QueryParam("error")   String error) {
  Contact c = contactRepo.find(id);
  if (c == null) throw new NotFoundException();
  String iso = (c.getBirthday() == null) ? "" :
               new java.text.SimpleDateFormat("yyyy-MM-dd").format(c.getBirthday());

  return formT
      .data("title", bundle().getString("EditContactTitle"))
      .data("action", "/contacts/" + id)
      .data("contact", c)
      .data("dateValue", iso)
      .data("bundle", bundle())
      .data("i18n", i18n())
      .data("errors", Collections.emptyMap())
      .data("errorList", Collections.emptyList())
      .data("success", success)
      .data("error", error)
      .render();
}


  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response create(@FormParam("firstName") String firstName,
                         @FormParam("lastName")  String lastName,
                         @FormParam("email")     String email,
                         @FormParam("mobilePhone") String mobilePhone,
                         @FormParam("homePhone")   String homePhone,
                         @FormParam("birthday")     String birthdayIso) {
    try {
      Contact c = new Contact();
      c.setFirstName(firstName);
      c.setLastName(lastName);
      c.setEmail(email);
      c.setMobilePhone(mobilePhone);
      c.setHomePhone(homePhone);
      if (birthdayIso != null && !birthdayIso.isBlank()) {
        c.setBirthday(java.sql.Date.valueOf(birthdayIso));
      }

      Set<ConstraintViolation<Contact>> v = validator.validate(c);
      if (!v.isEmpty()) {
        Map<String,String> errors = new LinkedHashMap<>();
        for (var cv : v) errors.put(cv.getPropertyPath().toString(), cv.getMessage());
        String html = formT
            .data("title", bundle().getString("CreateContactTitle"))
            .data("action", "/contacts")
            .data("contact", c)
            .data("dateValue", birthdayIso == null ? "" : birthdayIso)
            .data("bundle", bundle())
            .data("i18n", i18n())
            .data("errors", errors)
            .data("success", "")
            .data("error", "")
            .data("errorList", errors.values())
            .render();
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_HTML).entity(html).build();
      }

      contactRepo.create(c);
      return Response.seeOther(URI.create("/contacts?success=" + enc(bundle().getString("ContactCreated")))).build();

    } catch (Exception e) {
      e.printStackTrace();
      return Response.seeOther(URI.create("/contacts/new?error=" + enc(bundle().getString("PersistenceErrorOccured")))).build();
    }
  }

  @POST @Path("{id}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response update(@PathParam("id") long id,
                         @FormParam("firstName") String firstName,
                         @FormParam("lastName")  String lastName,
                         @FormParam("email")     String email,
                         @FormParam("mobilePhone") String mobilePhone,
                         @FormParam("homePhone")   String homePhone,
                         @FormParam("birthday")     String birthdayIso) {
    try {
      Contact ex = contactRepo.find(id);
      if (ex == null) throw new NotFoundException();

      ex.setFirstName(firstName);
      ex.setLastName(lastName);
      ex.setEmail(email);
      ex.setMobilePhone(mobilePhone);
      ex.setHomePhone(homePhone);
      ex.setBirthday((birthdayIso == null || birthdayIso.isBlank())
          ? null
          : java.sql.Date.valueOf(birthdayIso));

      Set<ConstraintViolation<Contact>> v = validator.validate(ex);
      if (!v.isEmpty()) {
        Map<String,String> errors = new LinkedHashMap<>();
        for (var cv : v) errors.put(cv.getPropertyPath().toString(), cv.getMessage());
        String iso = (ex.getBirthday()==null) ? "" : new SimpleDateFormat("yyyy-MM-dd").format(ex.getBirthday());
        
        String html = formT
            .data("title", bundle().getString("EditContactTitle"))
            .data("action", "/contacts/" + id)
            .data("contact", ex)
            .data("dateValue", iso)
            .data("bundle", bundle())
            .data("i18n", i18n())
            .data("errors", errors)
            .data("success", "")
            .data("error", "")
            .data("errorList", errors.values())
            .render();
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_HTML).entity(html).build();
      }

      contactRepo.edit(ex);
      return Response.seeOther(URI.create("/contacts/" + id + "?success=" + enc(bundle().getString("ContactUpdated")))).build();

    } catch (Exception e) {
      return Response.seeOther(URI.create("/contacts/" + id + "/edit?error=" + enc(bundle().getString("PersistenceErrorOccured")))).build();
    }
  }

  @POST @Path("{id}/delete")
  public Response delete(@PathParam("id") long id) {
    try {
      Contact ex = contactRepo.find(id);
      if (ex != null) contactRepo.remove(ex);
      return Response.seeOther(URI.create("/contacts?success=" + enc(bundle().getString("ContactDeleted")))).build();
    } catch (Exception e) {
      return Response.seeOther(URI.create("/contacts?error=" + enc(bundle().getString("PersistenceErrorOccured")))).build();
    }
  }
}
