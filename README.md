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

- [ ] Real user management
- [ ] Styling
- [ ] Add a start/splash page
- [ ] Allow sorting of games that where played on the same date
- [ ] Make it so that a bad insert request redirects back to the overview url
- [ ] Calculate and display current standings.

- [x] Only store actual game and player data. (Don't store current standings)
- [x] Support inserting of old games (i.e. recalculate all newer games' results)
- [x] Do not allow registration of games where the two players are the same player (e.g. Adam vs. Adam)
- [x] Validate data for new games server side
- [x] Don't allow creation of players with an empty name.
- [x] Don't store the logged in username in clear text in the cookie
- [x] Add basic security
- [x] Display current standings

