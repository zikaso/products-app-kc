package ma.enset.productsapp.web;

import ma.enset.productsapp.entities.Suppliers;
import ma.enset.productsapp.repositories.ProductRepository;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.hateoas.PagedModel;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProductController{
    @Autowired
    private ProductRepository productRepository;

    @Autowired       // this object from Bean created in the keycloakConfig calss
    private KeycloakRestTemplate keycloakRestTemplate;

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/products")
    public String products(Model model){
        model.addAttribute("products",productRepository.findAll());
        return "products";
    }

    @GetMapping("/suppliers")
    public String suppliers( Model model)
    {
       //By this way keycloak added automatically Authorization JWT Header
         PagedModel<Suppliers> pagedModelSuppliers = keycloakRestTemplate.getForObject("http://localhost:8083/suppliers", PagedModel.class);
         model.addAttribute("suppliers",pagedModelSuppliers);
        return "suppliers";
    }
     @ExceptionHandler(Exception.class)
    public  String exceptionHandler( Exception e , Model model){
            model.addAttribute("errorMessage" ,"Access Denied" );
         return "errors";
    }


    // this method just to get  token from  client session
    @GetMapping("/jwt")
    @ResponseBody    // this just for simple controller  not a rest  return json format
    public Map<String ,String> map(HttpServletRequest  request){
        KeycloakAuthenticationToken  token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal= (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext  session =principal.getKeycloakSecurityContext();
        Map<String,String> map = new HashMap<>();
       // test sand request by Rest Template to an other service with the Token that we get from session
      /*  RestTemplate  restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+session.getIdTokenString());
        HttpEntity httpEntity= new HttpEntity(httpHeaders);
        ResponseEntity responseEntity=restTemplate.exchange
                ("http://localhost:8081/suppliers", HttpMethod.GET,  HttpEntity, new ParameterizedTypeReference<Object>() {
                })*/

        map.put("access_token" ,session.getTokenString());
        return map;
    }





}
