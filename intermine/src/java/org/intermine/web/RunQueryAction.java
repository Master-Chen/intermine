package org.flymine.web;

/*
 * Copyright (C) 2002-2003 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.flymine.objectstore.ObjectStore;
import org.flymine.objectstore.ObjectStoreFactory;
import org.flymine.objectstore.query.Query;
import org.flymine.objectstore.query.Results;
import org.flymine.util.PropertiesUtil;

/**
 * Implementation of <strong>Action</strong> that runs a Query. The query to run
 * is passed in as a session parameter and the results are put on to the session as well.
 *
 * @author Andrew Varley
 */

public class RunQueryAction extends Action
{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return an ActionForward object defining where control goes next
     *
     * @exception Exception if the application business logic throws
     *  an exception
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {

        HttpSession session = request.getSession();

        // Remove any previous results from the session
        if (session.getAttribute("results") != null) {
            session.removeAttribute("results");
        }
        // Remove any previous displayableresults from the session
        if (session.getAttribute("resultsTable") != null) {
            session.removeAttribute("resultsTable");
        }
        Query q = (Query) session.getAttribute("query");

        Properties props = PropertiesUtil.getPropertiesStartingWith("objectstoreserver");
        props = PropertiesUtil.stripStart("objectstoreserver", props);
        String osAlias = props.getProperty("os");
        ObjectStore os = ObjectStoreFactory.getObjectStore(osAlias);

        Results results = os.execute(q);
        session.setAttribute("results", results);

        return (mapping.findForward("results"));

    }
}
