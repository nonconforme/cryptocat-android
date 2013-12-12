Cryptocat for Android
=================

**WARNING**: Cryptocat for Android is still in development. It has not received any kind of security audit yet, so **do NOT consider it to be secure yet**.

What's done
---

* Joining multiple servers and rooms.
* Sending and receiving multiParty messages 
* User list
* OTR (1-to-1) chat

What's not done (yet)
---

* Fingerprint showing
* File transfer
* Smilies
* Custom servers and server list
* Lots of navigation: 
   * connect/disconnect servers
   * Join/leave rooms
* Notifications
* Tablet layout

Known bugs (they will get fixed!)
---
* Auto-reconnection sometimes fails (?)
* No way to close the service (It should close when you disconnect from all servers)

How to build
---

You will need: 
* Java JDK 1.7 or older
* Android SDK
* IntelliJ Idea (any edition will work)
* Git

1. Clone the repo

```
    git clone --recursive git@github.com/cryptocat/cryptocat-android.git
```

2. Open the project in IntelliJ IDEA
3. Go to File -> Project Structure
4. If you see "Module SDK: Android Platform [invalid]", click New -> Android SDK
5. Choose the location of the Java and/or Android SDKs when asked.
6. Go to Run -> Edit Configurations
7. Click the "+" icon, then choose "Android Application"
8. Select Module "Cryptocat"
9. Choose whether you want to launch on emulator or USB device
10. Click OK.
11. Now you can compile and run Cryptocat from the IDE! Congratulations! :3

Awesome libraries used
---
* [ActionBarSherlock](http://actionbarsherlock.com)
* [SlidingMenu](https://github.com/jfeinstein10/SlidingMenu)
* [aSmack](https://github.com/flowdalic/asmack)
* [Google Gson](https://code.google.com/p/google-gson/)
* [SpongyCastle](http://rtyley.github.io/spongycastle/)
* [otr4j](https://code.google.com/p/otr4j/) (Modified to use SpongyCastle instead of BouncyCastle)
* Android Support v4

