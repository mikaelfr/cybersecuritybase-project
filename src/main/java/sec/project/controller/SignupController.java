package sec.project.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    
    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        signupRepository.save(new Signup(name, address));
        return "done";
    }

    @RequestMapping(value = "/signed", method = RequestMethod.GET)
    public String loadSigned(Model model) {
        List<String> mapped = signupRepository.findAll().stream().map(s -> s.getName()).collect(Collectors.toList());
        model.addAttribute("list", mapped);
        return "signed";
    }
    
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String loadAdmin(Model model) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //if (auth != null && auth.isAuthenticated() && auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            List<String> mapped = signupRepository.findAll().stream().map(s -> s.getName() + " - " + s.getAddress()).collect(Collectors.toList());
            model.addAttribute("list", mapped);
        //}
        
        return "admin";
    }
    
    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public String submitLogin(@RequestParam String username, @RequestParam String password) {
        if (userDetailsService.loadUserByUsername(username) != null && BCrypt.checkpw(password, userDetailsService.loadUserByUsername(username).getPassword())) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        return "redirect:/admin";
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.setViewName("error");
        return mav;
    }
}
