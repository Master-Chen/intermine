package org.intermine.web;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.io.PrintStream;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.intermine.objectstore.ObjectStore;
/**
 * Exports templates to XML.
 *
 * @author Thomas Riley
 */
public class TemplatesExportAction extends Action
{
    /**
     * @see Action#execute
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        HttpSession session = request.getSession();
        ServletContext servletContext = session.getServletContext();
        ObjectStore os = (ObjectStore) servletContext.getAttribute(Constants.OBJECTSTORE);
        Profile profile = (Profile) session.getAttribute(Constants.PROFILE);
        String type = request.getParameter("type");
        Map templates = null;
        
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition ", "inline; filename=template-queries.xml");
        
        if (type == null || type.equals("user")) {
            templates = profile.getSavedTemplates();
        } else if (type.equals("global")) {
            templates = (Map) servletContext.getAttribute(Constants.GLOBAL_TEMPLATE_QUERIES);
        } else {
            return null;
        }
        
        PrintStream out = new PrintStream(response.getOutputStream());
        out.print(TemplateHelper.templateMapToXML(templates));
        out.flush();
        return null;
    }
}
