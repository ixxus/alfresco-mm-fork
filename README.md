
Alfresco Media Management Module
===================

Description
-----------

This is a fork of the Media Management Module from Alfresco SVN, it is based on a version for Alfresco Community 4.2 and has been back ported to be compatabile with Enterprise 4.1.1

This extension adds media / digital asset management (DAM) features to the Alfresco repository and Share interface.

Requirements
------------

- Maven 3
- Access to internal and enterprise Alfresco maven repositories (this should change soon)
- [FFmpeg](http:/ffmpeg.org) must be available on the command line for video transformations.  You can override the default
path by setting `ffmpeg.exe` in your global properties.

Build
-----

(Examples below should be executed from within the root `mediamanagement` folder.)

### Default (no tests)

The default behavior is to skip tests unless a `runTests` is specified, so
after calling:

    mvn clean package



Installation
------------

if on a Maven based project add the two Jars as dependencies otherwise copy them into the libs directories of Alfresco and Share.

You must also add the following lines to your alfresco-global.properties file:
system.thumbnail.definition.default.timeoutMs=-1
system.thumbnail.definition.default.readLimitTimeMs=-1
system.thumbnail.definition.default.maxSourceSizeKBytes=-1
system.thumbnail.definition.default.readLimitKBytes=-1
system.thumbnail.definition.default.pageLimit=1
system.thumbnail.definition.default.maxPages=-1

