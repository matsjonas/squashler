squashler
=========

Squash ladder web application

## Information

Squashler is written using [Play Framework](http://www.playframework.com/). It is currently in development and can only be run on machines with Play installed.

To run squashler using Play, stand in the squashler directory and type:

```
$ play run
```

## TODO:

- [ ] Don't store the logged in username in clear text in the cookie
- [ ] Real user management
- [ ] Styling
- [ ] Add a start/splash page
- [ ] Do not allow registration of games where the two players are the same player (e.g. Adam vs. Adam)
- [ ] Allow sorting of games that where played on the same date
- [ ] Support inserting of old games (i.e. recalculate all newer games' results)

- [x] Add basic security
- [x] Display current standings

