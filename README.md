# OPADemo

A simple demo of using OPA within a domain API.  We'll model a very simplistic API for
earnings data.

There are three projects in the demo

## opa
This project defines the OPA rego for the demo

## relationship-server
This project defines a simple node API to reflect the relationships between managers
and their distributors.

## EarningsAPIDemo
This project defines the actual API.

## TODO

- ~~add date effectivity to the affiliate to affiliate relationships and incorporate into the query~~
- fix the policy to determine which relationship API to call (office vs. person) rather than passing
the silly officeUser flag.