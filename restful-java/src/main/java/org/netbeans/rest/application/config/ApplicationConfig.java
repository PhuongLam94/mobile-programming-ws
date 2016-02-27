package org.netbeans.rest.application.config;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import vn.khmt.restful.UserService;
import vn.khmt.restful.COSRFilter;
/**
 *
 * @author The Nhan
 */
@ApplicationPath("")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(UserService.class);
        s.add(COSRFilter.class);
        return s;
    }
}
