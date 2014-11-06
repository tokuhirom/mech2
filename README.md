# mech2
[![Build Status](https://travis-ci.org/tokuhirom/mech2.svg)](https://travis-ci.org/tokuhirom/mech2)

## SYNOPSIS

    Mech2 mech2 = Mech2.builder().build();
    Mech2WithBase wb = new Mech2WithBase(mech2, baseURI);
    assertThat(
      wb.get("/")
        .execute()
        .getResponse()
        .getStatusLine()
        .getStatusCode(), is(200));
    assertThat(
      wb.get("/")
        .execute()
        .getResponseBodyAsString(), is("HAHAHA"));

## DESCRIPTION

mech2 is a HTTP client library like Faraday.

## javadoc

You can see the javadoc on javadoc.io

http://www.javadoc.io/doc/me.geso/mech2/

## FAQ

### How do I disable redirect handling?

You can call `Mech2#disableRedirectHandling()` or `Mech2WithBase#disableRedirectHandling()`.

## AUTHORS

Tokuhiro Matsuno, tokuhirom@gmail.com

## LICENSE

    The MIT License (MIT)
    Copyright © 2014 Tokuhiro Matsuno, http://64p.org/ <tokuhirom@gmail.com>

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the “Software”), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
