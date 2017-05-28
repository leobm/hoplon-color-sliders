# colors-sliders

A [Hoplon][3] port of Mark Harwood's [VueJS Colour Conversion Example][4].

## Dependencies

- java 1.7+

## Usage
### Development

1. lein figwheel

2. Go to [http://localhost:3449][2] in your browser. You should see the color slider.

3. If you edit and save a file, the figwheel will recompile the code and reload the
   browser to show the updated version.

### Production
1. Run lein uberjar

2. The compiled files will be on the `target/` directory. This will use
   advanced compilation and optimization

## License

Copyright © 2017, Jan-Felix Wittmann

[1]: https://leiningen.org/
[2]: http://localhost:3449]
[3]: http://hoplon.io
[4]: http://jsfiddle.net/Phunky/xhhmmrpo/
