ManyWho Huddle Service
======================

> This service is currently in development, and not yet recommended for use in production environments

[![Build Status](https://travis-ci.org/manywho/service-huddle.svg)](https://travis-ci.org/manywho/service-huddle)

This service allows you to integrate your Flows with [Huddle](https://www.huddle.com).

## Running

### Heroku 

The service is compatible with Heroku, and can be deployed by clicking the button below:

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/manywho/service-huddle)

### Locally

To build the service, you will need to have Apache Ant, Maven 3 and a Java 8 implementation installed, OpenJDK or Oracle JDK.

You will need to generate a configuration file for the service by running the provided `build.xml` script with Ant, and 
passing in some valid settings:

```bash
$ ant -Doauth2.clientId=xxx
```

Now you can build the runnable shaded JAR.

#### Defaults

Running the following command will start the service listening on `0.0.0.0:8080/api/huddle/1`:

```bash
$ java -jar target/huddle-1.0-SNAPSHOT.jar
```

#### Custom Port

You can specify a custom port to run the service on by passing the `server.port` property when running the JAR. The
following command will start the service listening on port 9090 (`0.0.0.0:9090/api/huddle/1`):

```bash
$ java -Dserver.port=9090 -jar target/huddle-1.0-SNAPSHOT.jar
```

## Contributing

Contributions are welcome to the project - whether they are feature requests, improvements or bug fixes! Refer to 
[CONTRIBUTING.md](CONTRIBUTING.md) for our contribution requirements.

## License

This service is released under the [MIT License](http://opensource.org/licenses/mit-license.php).