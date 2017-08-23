# clj-lambda-graphviz-s3

In this project, my goal is to create an aws lambda function, which performs dot graph rendering in the cloud.

I use Clojure for function implementation. That function takes graph description, generates graph using c binary library "Graphviz" and saves graph to S3 storage.

I used my lambada library for AWS Lambda integration, clojure.java.shell to call graphviz executable.

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

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
