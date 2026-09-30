package com.idem;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Application {

    private static final String PAGE = """
        <!doctype html>
        <html lang="en">
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>IDEM Hello</title>
          <style>
            * {
              box-sizing: border-box;
            }

            body {
              margin: 0;
              min-height: 100vh;
              display: flex;
              align-items: center;
              justify-content: center;
              font-family: Arial, sans-serif;
              background: #0b0f19;
              color: #ffffff;
            }

            .card {
              width: min(520px, calc(100% - 32px));
              padding: 40px;
              border-radius: 24px;
              border: 1px solid rgba(255,255,255,0.08);
              background: #121827;
              box-shadow: 0 24px 80px rgba(0,0,0,0.35);
            }

            .status {
              display: inline-flex;
              align-items: center;
              gap: 8px;
              font-size: 13px;
              color: #b8c1d9;
            }

            .dot {
              width: 9px;
              height: 9px;
              border-radius: 50%;
              background: #54e38e;
            }

            h1 {
              margin: 24px 0 12px;
              font-size: 42px;
            }

            p {
              margin: 0;
              color: #939db5;
              line-height: 1.6;
            }

            .grid {
              margin-top: 28px;
              padding-top: 24px;
              border-top: 1px solid rgba(255,255,255,0.08);
              display: grid;
              grid-template-columns: 1fr 1fr;
              gap: 20px;
            }

            .label {
              font-size: 12px;
              color: #67728d;
              text-transform: uppercase;
              letter-spacing: 0.08em;
            }

            .value {
              margin-top: 6px;
              font-size: 14px;
            }
          </style>
        </head>

        <body>
          <main class="card">
            <div class="status">
              <span class="dot"></span>
              Kubernetes deployment is healthy
            </div>

            <h1>Hello from IDEM</h1>

            <p>
              This page is served by a tiny Java application
              running inside the IDEM Kubernetes cluster.
            </p>

            <div class="grid">
              <div>
                <div class="label">Service</div>
                <div class="value">idem-hello</div>
              </div>

              <div>
                <div class="label">Environment</div>
                <div class="value">dev</div>
              </div>

              <div>
                <div class="label">Runtime</div>
                <div class="value">Java 21</div>
              </div>

              <div>
                <div class="label">Deployment</div>
                <div class="value">Argo CD</div>
              </div>
            </div>
          </main>
        </body>
        </html>
        """;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", 8080),
                0
        );

        server.createContext("/", exchange -> {
            byte[] response = PAGE.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders()
                    .set("Content-Type", "text/html; charset=utf-8");

            exchange.sendResponseHeaders(200, response.length);

            try (var output = exchange.getResponseBody()) {
                output.write(response);
            }
        });

        server.createContext("/health", exchange -> {
            byte[] response = "OK".getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders()
                    .set("Content-Type", "text/plain; charset=utf-8");

            exchange.sendResponseHeaders(200, response.length);

            try (var output = exchange.getResponseBody()) {
                output.write(response);
            }
        });

        server.start();

        System.out.println("IDEM hello started on port 8080");
    }
}