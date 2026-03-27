package service.gateway.Security;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class RouteDebugController {

    private final RouteLocator routeLocator;

    public RouteDebugController(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @GetMapping("/routes")
    public Flux<String> listRoutes() {
        return routeLocator.getRoutes()
                           .map(r -> r.getId() + " -> " + r.getUri());
    }
}