<h1 align="center">🔋 Anti Theft Charge</h1>
<p align="center">
  <i>Safely charge your phone in public places without the worry of it being stolen</i>
</p>

<p align="center">
  <table align="center">
    <tr>
      <td>
        <img width="300" src="demo/atc-initial-app-start.gif" title="Launching app and starting service" />
        <img width="300" src="demo/atc-device-protected.gif" title="Arming and unplugging device" />
      </td>
    <tr>
  </table>
</p>


[![codebeat badge](https://codebeat.co/badges/f920568a-0d1c-444f-ae62-9e06ff23b351)](https://codebeat.co/projects/github-com-lissy93-anti-theft-charge-master)


### Intro
Ever need to charge your phone in a pubic space, like a train, hostel or airport? Worried that if your eyes leave your device for just a few seconds it could be snatched? ATC is a simple app, that once enabled will sound an alarm at top volume if your device is unplugged when the screen is off /locked. This should be enough to deter the thief and draw your attention.

<details>
<summary><b>About</b></summary>

Developed in 24 hours at AngelHack London 2014, won 2nd prize 🏆. Had 100,000 downloads on the Play Store 📲, before it was removed for not having a Privacy Policy 😅. Rewrote it in Kotlin in 2017, just for fun, while I was in hosipital after an accident (i was high af 💊). But decided not to publish it, since there is enough crap on the App Store already 💩.
But if you'd like to use it, [here is the APK](demo/app-release.apk). The source was last updated in 2019.

I used to sleep in hostels, I'd have  my phone on charge by my pillow. After I wrote this app, there were several occasions when people tried to take my device, but they got the fright of their life when the loud alarm went off, each time dropping it and running off 🏃, or claiming they thought it was their phone 😂. So it served it's purpose well 🙌.

</details>

### How it works
All the logic is in [this directory](app/src/main/java/com/aliciasykes/anti_theft_charge). When the state is set to armed, a service runs in the background watching for power changes and calling an intent that starts the alarm accordingly.

 - [**`ArmDisarmFunctionality.kt`**](app/src/main/java/com/aliciasykes/anti_theft_charge/ArmDisarmFunctionality.kt) - Manages the UI animations.
 - [**`AlarmUtil.kt`**](app/src/main/java/com/aliciasykes/anti_theft_charge/AlarmUtil.kt) - Manages the alarm functionality
 - [**`ChargingUtil.kt`**](app/src/main/java/com/aliciasykes/anti_theft_charge/ChargingUtil.kt) - Determines connectivity status
 - [**`NotificationUtil.kt`**](app/src/main/java/com/aliciasykes/anti_theft_charge/NotificationUtil.kt) - Handles the notification functionality
 - [**`PowerConnectionService.kt`**](app/src/main/java/com/aliciasykes/anti_theft_charge/PowerConnectionService.kt) - Watches for power connectivity changes
 - [**`Restarter.kt`**](app/src/main/java/com/aliciasykes/anti_theft_charge/Restarter.kt) - Keeps the app running in the background, when it is armed


### Contributing
If you want to improve something, add a feature or fix a bug, feel free to submit a [pull request](https://github.com/Lissy93/anti-theft-charge/compare), or [raise an issue](https://github.com/Lissy93/anti-theft-charge/issues/new). Thank you!


### License
This project is open source, feel free to do anything you like with the code.
[Licensed under MIT](LICENSE.md).

---

<p  align="center">
  <i>© <a href="https://aliciasykes.com">Alicia Sykes</a> 2014 - 2019</i><br>
  <i>Licensed under <a href="https://gist.github.com/Lissy93/143d2ee01ccc5c052a17">MIT</a></i><br>
  <a href="https://github.com/lissy93"><img src="https://i.ibb.co/4KtpYxb/octocat-clean-mini.png" /></a>
</p>


<!-- Dinosaur -->
<!-- 
                        . - ~ ~ ~ - .
      ..     _      .-~               ~-.
     //|     \ `..~                      `.
    || |      }  }              /       \  \
(\   \\ \~^..'                 |         }  \
 \`.-~  o      /       }       |        /    \
 (__          |       /        |       /      `.
  `- - ~ ~ -._|      /_ - ~ ~ ^|      /- _      `.
              |     /          |     /     ~-.     ~- _
              |_____|          |_____|         ~ - . _ _~_-_
-->
