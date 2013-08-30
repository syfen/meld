syfen / meld
====

Java Archaius Zookeeper Configuration implementation designed to support auto scaling Zookeeper clusters.

## What is meld about?

Meld is a very small library which strives to bring together the following components:

- Zookeeper
- Exhibitor
- Curator
- Archaius
- Amazon Web Services
    - Autoscaling (zookeeper cluster)
    - S3 (for shared exhibitor configuration)

If all of these things are part of your stack then syfen meld will probably interest you. If not, you may be better off working with something else.
Meld is inspired by [cfregly's](https://github.com/cfregly) [fluxcapacitor](https://github.com/cfregly/fluxcapacitor) project. Specifically his [ZooKeeperClientFactory](https://github.com/cfregly/fluxcapacitor/blob/master/flux-core/src/main/java/com/fluxcapacitor/core/zookeeper/ZooKeeperClientFactory.java) class which I have adapted to:

- be a little more generic allowing multiple Zookeeper namespaces and root paths to be used by a single application
- be integrated with Exhibitor by using the ExhibitorEnsembleProvider
- leverage the Exhibitor shared configuration file (stored in S3) to support the initialization of the Exhibitor Ensemble Provider and the Backup Connection String Provider

My goal is to enable the use of an Autoscaling Zookeeper Cluster which can have individual nodes terminated while continuing to function and self heal. This goal has been met however at this stage it may be somewhat academic as this solution relies upon Zookeeper being accessed via Curator which may not be the case when other applications connect to the Zookeeper cluster.

[Wiki](https://github.com/syfen/meld/wiki) starts [here](https://github.com/syfen/meld/wiki).

