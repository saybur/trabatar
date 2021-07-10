trabatar-shield
===============

**IMPORTANT WARNING: THIS HARDWARE IS UNTESTED!**

A very simple shield for [Arduino Nano](https://store.arduino.cc/usa/arduino-nano)
providing an [Apple Desktop Bus](https://en.wikipedia.org/wiki/Apple_Desktop_Bus)
connection. This allows software running on the Arduino (or a connected
computer) to communicate across ADB with a retro computer or peripherals.

Components
----------

The goal of this hardware is a simple, very easy-to-assemble board. The
following components are needed.

* 1x TE 5749181-1 4-pin Mini-DIN connector
* 2x 2N3904 transistors
* 3x 4.7Kohm resistors
* 1x 100Kohm resistor
* 2x 15-pin 100mil female headers

One 4.7Kohm resistor (R2) is used to help pull-up the ADB data line to 5V,
along with the resistor in the ADB host. Another 4.7Kohm resistor (R1) is used
to limit the current through the base of of the transistor (Q1) for asserting
the ADB data line, and the final 4.7Kohm resistor (R3) is used in the same
fashion for the transistor (Q2) on the ADB power line. The 100Kohm resistor
(R4) helps bias the data line low to prevent the bus floating when remote
device(s) are off or disconnected. These specific resistor values are not
critical and similar values would likely work fine, but have not been tested.

The transistors may also be replaced with other TO-96 / TO-226 bipolar NPN
transistors having the same pinout and similiar characteristics, particularly
the ability to come out of saturation within a few microseconds. Potential
examples include the PN2222, though that part is untested.

Limitations
-----------

The simplicity of this board means there are a number of things to be aware of
when using it.

* No electrostatic discharge protection is present apart from what is offered
  by the AVR microcontroller.
* There is no protection against [latch-up](https://en.wikipedia.org/wiki/Latch-up)
  caused by the ADB host starting before the Arduino board. Users must ensure
  the board is powered before any ADB device(s).
* If the USB 5V rail is 0.05 volts or lower relative to ADB 5V, the circuit may
  violate the V<sub>IH</sub> rating of the AVR due to the forward voltage drop
  of the USB rectifier diode.
* There is a common ground between the Arduino and connected ADB devices, as
  well as any connected USB host.

There are likely other caveats the author is unaware of. User contributions are
welcome, both in knowledge and design.

License
-------

These files are available under the terms of the GNU General Public License,
version 3, available in the LICENSE document in the parent directory. There is
NO WARRANTY, not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
For details, refer to the license.

