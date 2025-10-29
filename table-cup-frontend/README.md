# Installed Dependencies

You can find the installed dependencies in the [package.json](package.json) file.

# Backend connection

## Local machine

The connection to the backend is configured in the [environment.js](src/environments/environment.ts) file.

## Production build

The connection to the backend is configured in the [environment.prod.js](src/environments/environment.prod.ts) file.

# Run the frontend

## Local machine

### Presiquisites

1. Node.js Version 22.20.0 installed.
2. NPM Version 11.6.1 installed.

### Run

1. Open a terminal in the project folder.
2. Run `npm install -g @ionic/cli@8.7.7` to install the ionic cli.
3. Run `npm install` to install all dependencies.
4. Run `ionic serve` to start the frontend.

## Docker

### Prerequisites

1. Docker Desktop installed.
2. Backend and Database running in Docker containers. See README in backend project.

### Run

1. Open a terminal in the project folder.
2. Run `docker build -t table-cup-frontend .` to build the image.
3. Run `docker run -p 80:80 table-cup-frontend` to run the image.
4. Open a browser and navigate to http://localhost to call the frontend.
