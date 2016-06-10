/* The MIT License (MIT)
 *
 * Copyright (c) 2016 Tyler Fitch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.chef.bamboo.admin;

import io.chef.bamboo.ChefIdentity;
import io.chef.bamboo.ChefIdentityService;

import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.servlet.BambooHttpServlet;
import com.atlassian.bamboo.template.TemplateRenderer;
import com.atlassian.plugin.web.renderer.RendererException;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * This class presents the UI in the admin area of Bamboo.
 * TODO: we'll use Bandana directly instead of REST calls
 * It in turns uses JS to make async calls to {@link ChefIdentityConfigResource}
 * which returns a {@link ChefIdentity} object as JSON to the UI.
 *
 * @author tfitch on 6/9/16.
 */
public class AdminServlet extends BambooHttpServlet {
    public static String ACTION_EDIT = "edit";
    public static String ACTION_ADD = "edit";
    public static String ACTION_DELETE = "edit";

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AdminServlet.class);

    private final UserManager userManager;
    private final TemplateRenderer templateRenderer;
    private final LoginUriProvider loginUriProvider;
    private final PlanManager planManager;
    private final ChefIdentityService chefIdentityService;

    public AdminServlet (UserManager userManager,
                         LoginUriProvider loginUriProvider,
                         TemplateRenderer templateRenderer,
                         PlanManager planManager,
                         ChefIdentityService chefIdentityService) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.templateRenderer = templateRenderer;
        this.planManager = planManager;
        this.chefIdentityService = chefIdentityService;
    }

    @Override
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // from my example (buildBunny), rolling my own auth.  Probably in built function for this
        UserProfile userProfile = userManager.getRemoteUser(request);  // getRemoteUsername(request);
        if (userProfile == null) {
            redirectToLogin(request, response);
            return;
        }

        response.setContentType("text/html;charset=utf-8");
        String action = request.getParameter("action");
        if (!StringUtils.isEmpty(action)) {
            String idName = request.getParameter("idName");
            if (ACTION_EDIT.equalsIgnoreCase(action) && !StringUtils.isEmpty(idName)) {
                generateEditPageForChefIdentity(idName, response, userProfile);
            } else if (ACTION_DELETE.equalsIgnoreCase(action) && !StringUtils.isEmpty(idName)) {
                if (deleteChefIdentity(idName, userProfile)) {
                    response.sendRedirect(request.getRequestURI());
                }
            } else if (ACTION_ADD.equalsIgnoreCase(action)) {
                generateEditPageForChefIdentity(null, response, userProfile);
            } else {
                generateUnsupportedOperation(action);
            }
        } else {
            Map<String,Object> chefIdentityMap = chefIdentityService.getChefIdentityMap(null);

            templateRenderer.render("templates/adminSummary.vm", chefIdentityMap, response.getWriter());
        }


    }

    private boolean deleteChefIdentity (String idName, UserProfile userProfile) {
        chefIdentityService.deleteChefIdentity(idName);
        return true;
    }

    private void generateEditPageForChefIdentity (String idName, HttpServletResponse response, UserProfile userProfile) throws RendererException, IOException {

        Map<String,Object> chefIdentityMap = chefIdentityService.getChefIdentityMap(idName);

        templateRenderer.render("templates/chefIdentityEdit.vm", chefIdentityMap, response.getWriter());
    }

    private void generateUnsupportedOperation(String action) {
        // FIXME (because buildBunny says fix it)
        LOG.error("Unsupported Chef Identity administrative action of " + action);
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }

    private URI getUri(HttpServletRequest request) {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }
}
