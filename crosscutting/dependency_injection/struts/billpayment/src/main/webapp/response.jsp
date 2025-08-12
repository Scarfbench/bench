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
                <title>Bill Payment: Result</title>
                <link rel="stylesheet"
                      type="text/css"
                      href="<s:url value='/css/default.css'/>" />
            </head>

            <body>
                <h2>Bill Payment: Result</h2>
                <h3>Amount Paid with
                    <s:if test="paymentOption == 1">
                        Debit Card:
                    </s:if>
                    <s:elseif test="paymentOption == 2">
                        Credit Card:
                    </s:elseif>
                    <s:text name="format.currency">
                        <s:param name="value"
                                 value="value" />
                    </s:text>
                </h3>

                <s:form>
                    <s:submit value="Back"
                              action="index" />
                </s:form>
            </body>

            </html>