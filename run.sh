#!/bin/bash

sbt compile publishLocal "runMain RawImporter.RawImporterApp $1"
