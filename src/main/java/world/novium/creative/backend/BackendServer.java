package world.novium.creative.backend;

import io.javalin.Javalin;

public class BackendServer implements Runnable {
    private final int port;
    private final String authToken;
    private Javalin app;

    public BackendServer(int port, String authToken) {
        this.port = port;
        this.authToken = authToken;
    }

    @Override
    public void run() {
        app = Javalin.create(config -> config.router.apiBuilder(
                new WorldsEndpoint()
        ));

        // check bearer auth
        app.before(ctx -> {
            String authHeader = ctx.header("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                ctx.status(401).result("Unauthorized");
                return;
            }
            String token = authHeader.substring(7);
            // Here you would validate the token, for now we just check if it's "valid-token"
            if (!authToken.equals(token)) {
                ctx.status(403).result("Forbidden");
            }
        });


        app.start(port);

    }

    public void stop() {
        if (app != null) {
            app.stop();
            app = null;
        }
    }
}
