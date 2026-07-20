# HTTP Server

A multithreaded HTTP/1.1 server written from scratch in Java to understand how web servers process requests internally.

Instead of relying on frameworks like Spring Boot or Netty, this project implements the complete request lifecycle using Java sockets, request parsing, response construction, and concurrent connection handling.

Current implementation focuses on correctness, readability, and HTTP fundamentals rather than production performance.

---

## Features

- HTTP/1.1 Request Parsing
- Concurrent request handling using worker threads
- Static file serving
- GET support
- HEAD support
- MIME type detection
- Builder Pattern for HTTP response creation
- Proper HTTP status codes
- Exception-based error handling
- Modular architecture

---

# Request Lifecycle

```text
                Client (Browser)
                       │
                       │ TCP Connection
                       ▼
             HttpServer (ServerSocket)
                       │
             accepts incoming socket
                       ▼
             ServerListener Thread
                       │
             creates worker thread
                       ▼
      HttpConnectionWorkerThread
                       │
          reads InputStream
                       ▼
                HttpParser
                       │
        validates HTTP request
        parses:
          • Method
          • Target
          • Version
          • Headers
                       ▼
                HttpRequest
                       │
          routes request method
                       ▼
           GET / HEAD Handler
                       │
             WebRootHandler
          locate requested file
                       │
       read bytes + determine MIME
                       ▼
             HttpResponse.Builder
                       │
      constructs HTTP compliant response
                       ▼
             OutputStream
                       │
                       ▼
                 Browser
```

---

# Architecture

```text
                    +----------------+
                    |   HttpServer   |
                    +----------------+
                            │
                            ▼
               +------------------------+
               | ServerListener Thread  |
               +------------------------+
                            │
          accepts incoming client socket
                            │
          creates one worker per request
                            ▼
        +----------------------------------+
        | HttpConnectionWorkerThread       |
        +----------------------------------+
                 │                  │
                 │                  │
                 ▼                  ▼
          HttpParser         WebRootHandler
                 │                  │
                 ▼                  ▼
            HttpRequest      Static Resources
                 │
                 ▼
         Request Dispatcher
                 │
        GET / HEAD Routing
                 │
                 ▼
      HttpResponse.Builder
                 │
                 ▼
          Serialized Response
                 │
                 ▼
              OutputStream
```

---

# Component Responsibilities

## HttpServer

- Opens the ServerSocket
- Starts the listener thread
- Owns server lifecycle

---

## ServerListener

Responsible for accepting client connections.

Instead of processing requests itself, it immediately delegates work to a new worker thread, allowing multiple clients to connect simultaneously.

---

## HttpConnectionWorkerThread

The core execution unit of the server.

Responsibilities:

- Read socket InputStream
- Parse HTTP request
- Route request by HTTP method
- Access requested resource
- Build HTTP response
- Write response to OutputStream
- Close resources safely

Each incoming connection gets its own worker thread.

---

## HttpParser

Responsible for converting raw bytes into a structured HttpRequest object.

The parser validates the request according to HTTP syntax and extracts

- Method
- Request Target
- HTTP Version
- Headers

Separating parsing from networking keeps responsibilities isolated and improves testability.

---

## HttpRequest

Represents an immutable parsed HTTP request.

Contains

- Method
- Version
- Request Target
- Headers

This object becomes the contract used by the rest of the server.

---

## WebRootHandler

Abstracts filesystem access.

Responsibilities

- Locate requested file
- Read file bytes
- Determine MIME type
- Throw meaningful exceptions

The networking layer never directly interacts with the filesystem.

---

## HttpResponse Builder

Responses are created using the Builder Pattern.

Instead of constructing responses manually, the builder assembles

- Status Line
- Headers
- Message Body

before serializing the response into bytes.

Example flow

```
Builder
    .httpVersion(...)
    .statusCode(...)
    .addHeader(...)
    .messageBody(...)
    .build();
```

This mirrors how many production frameworks construct HTTP responses internally.

---

# Supported Methods

| Method | Status |
|---------|--------|
| GET | ✅ |
| HEAD | ✅ |

---

# Supported Status Codes

| Code | Description |
|------|-------------|
| 200 | OK |
| 404 | Not Found |
| 500 | Internal Server Error |
| 501 | Not Implemented |

---

# Error Handling

The server separates protocol errors from filesystem errors.

### 404

Returned when the requested resource does not exist.

### 500

Returned when a resource exists but cannot be read.

### 501

Returned for unsupported HTTP methods.

---

# Concurrency Model

The server follows a thread-per-connection model.

```text
Client 1
            \
Client 2 -----> Worker Thread
            /
Client 3

Client 4 ----------> Worker Thread

Client 5 ----------> Worker Thread
```

Advantages

- Simple architecture
- Easy to understand
- Demonstrates Java threading fundamentals
- Independent request processing

---

# Design Principles

- Single Responsibility Principle
- Separation of Concerns
- Builder Pattern
- Exception-driven error handling
- Immutable request representation
- Modular package structure

---

# HTTP Request Flow

```
Socket

↓

InputStream

↓

HttpParser

↓

HttpRequest

↓

Method Dispatch

↓

WebRootHandler

↓

HttpResponse.Builder

↓

OutputStream

↓

Browser
```

---

# Future Improvements

- POST
- PUT
- DELETE
- Persistent Connections (Keep-Alive)
- Thread Pool Executor
- HTTP/1.1 Connection Reuse
- Chunked Transfer Encoding
- Directory Listing
- Request Logging
- Access Logs
- Virtual Hosts
- Routing Engine
- HTTPS
- HTTP/2

---

# Learning Outcomes

This project demonstrates understanding of

- Java Networking
- TCP Socket Programming
- HTTP/1.1 Protocol
- Request Parsing
- Response Serialization
- Concurrent Programming
- Builder Pattern
- Exception Handling
- Static File Serving
- Thread-per-Connection Server Design

---

## Project Goal

The objective was not to recreate an enterprise web server, but to understand the mechanics hidden behind frameworks like Spring Boot, Tomcat, and Netty.

By implementing the request lifecycle manually—from raw TCP sockets to HTTP-compliant responses—the project provides a deeper understanding of how web servers process, validate, route, and respond to client requests.