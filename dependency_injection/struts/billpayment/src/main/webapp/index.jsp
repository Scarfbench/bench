<%@ page
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    %>
    <%@ taglib
        prefix="s"
        uri="/struts-tags"
        %>
        <%-- Copyright
             (c),
             Eclipse
             Foundation,
             Inc.
             and
             its
             licensors.
             All
             rights
             reserved.
             This
             program
             and
             the
             accompanying
             materials
             are
             made
             available
             under
             the
             terms
             of
             the
             Eclipse
             Distribution
             License
             v1.0,
             which
             is
             available
             at
             https://www.eclipse.org/org/documents/edl-v10.php
             SPDX-License-Identifier:
             BSD-3-Clause
             --%>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport"
                      content="width=device-width, initial-scale=1.0">
                <title>Bill Payment Options</title>
                <link rel="stylesheet"
                      type="text/css"
                      href="<s:url value='/css/default.css'/>" />
            </head>

            <body>
                <h3>Bill Payment Options</h3>
                <p>Enter an amount, select Debit Card or Credit Card, then click Pay.</p>

                <s:form action="payment"
                        method="post">
                    <p>
                        <s:textfield name="value"
                                     label="Amount: $"
                                     required="true"
                                     requiredMessage="An amount is required."
                                     maxlength="15" />
                    </p>

                    <s:radio name="paymentOption"
                             label="Options:"
                             list="#{1:'Debit Card', 2:'Credit Card'}"
                             value="1" />

                    <p>
                        <s:submit value="Pay"
                                  action="payment" />
                    </p>

                    <p>
                        <s:submit value="Reset"
                                  action="reset" />
                    </p>
                </s:form>

                <div class="messagecolor">
                    <s:actionerror cssStyle="color: #d20005" />
                    <s:fielderror cssStyle="color: #d20005" />
                    <s:actionmessage cssStyle="color: blue" />
                </div>
            </body>

            </html>