trabatar
========

A simple Java program to share a modern computer's keyboard and mouse with a
classic Mac over an ADB connection. This lets you control your old Mac
sitting next to your main computer without keeping two sets of input
devices around.

The hardware required for this connection is
[trabular](https://github.com/saybur/trabular).  Please see that project
for information about what you need to create to make this work.

This software is written in Java, so it will build and run on any platform with
JavaSE 1.7 or newer, but since it writes directly to the serial port device
name it will only really be useful under Linux.  Something like
[RXTX](http://rxtx.qbang.org) will likely be added at some point.

This is in an early stage of development and will change significantly.
More documentation will be added soon.  Any feedback is appreciated.

License
-------

Except where otherwise noted, all files in this repository are available under
the terms of the GNU General Public License, version 3, available in the
LICENSE document. There is NO WARRANTY, not even for MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE. For details, refer to the license.
