# Generated Code for googles discovery apis

This uses the sbt plugin found [here](https://github.com/hamnis/google-discovery-scala) to generate 
case classes and clients for google apis that follow the discovery api spec.

Most of Google's apis are [disoverable](https://discovery.googleapis.com/discovery/v1/apis)
and if you need a new api, you can add that by submitting a pull request which adds the api to this project.

## Currently generated apis
- bigquery

## How to add a new api

1. Find the api that you want to add, either from the disovery list above, or by running
   `sbt printDiscoveryProject`
   Make sure that you add the project both as a `lazy val` AND in the root project `aggregates` list.
2. After the project has been added, run `sbt discoveryFetch`
3. Open a Pull request with the changes above
4. Wait a bit
5. I will merge the PR
6. I will publish the main branch after merge
7. You can start using the api about 20 minutes after the release process has been completed ( maven central sync )
