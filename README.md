# clj-lambda-graphviz-s3

In this project, my goal is to create an aws lambda function, which performs dot graph rendering in the cloud.

I use Clojure for function implementation. That function takes graph description, generates graph using c binary library "Graphviz" and saves graph to S3 storage.

I used my lambada library for AWS Lambda integration, clojure.java.shell to call graphviz executable.

My plan was:

1. Buuild Graphviz executable binary, that i can run in AWS Lambda environment.
2. Place binary under resources, so it will be packed into uberjar.
3. During runtime, copy binary under /tmp folder in AWS Lambda.
4. Run graph generation to temp file.
5. Put file to s3 and remove temp file.

## How to build Graphviz with pdf support.

We use graphviz library. We have to compile graphviz in the same platform, that we going to use to run it. (AWS Lambda) I have rent EC2 instance and compiled library in it.

```
$ // Rent ec2 instance.
$ // Connect to it using ssh.
$ yum groupinstall "Development tools" // that will install C compiler and delepment tools.
$ sudo yum install cairo-devel pango-devel // that dependencies required for PDF format.
$ wget http://www.graphviz.org/pub/graphviz/stable/SOURCES/graphviz-2.40.1.tar.gz
$ tar -xvf graphviz-2.40.1.tar.gz
$ cd graphviz-2.40.1
$ ./configure 
$ make 
$ cd cmd/dot
$ make dot_static
// To copy file from ssh to your local machine use next command
$ scp ec2-user@<place-your-number>.eu-central-1.compute.amazonaws.com:/home/ec2-user/graphviz/graphviz-2.40.1/cmd/dot/dot_static ./
```

## Execute graphviz using clojure.shell

The problem i faced is how to install graphviz in AWS Lambda environment. Because you do not have access to machine itself. What i decided to is to generate graphviz binary, place it under resources, and then copy under /tmp folder. From there we can call it as binary executable.

To execure graphviz binary i use [clojure.java.shell](https://clojure.github.io/clojure/clojure.java.shell-api.html). For that purpose i generate sh script and feed it to sh function.

## License

Copyright © 2017 Aleh Atsman

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
