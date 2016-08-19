# Simple Call Blocker
## Problem statement:
As soon as I started my career, I was so happy to get a mobile number and a bank account of my own. Apart from the good perks of having them, I was also equally annoyed by the promotion calls that I receive in my mobile number as a part of holding a bank account, related to stupid offers that are in no way related to banking and other investment related things. Even though DND were an option to block all the promotional calls and SMSs instantly. I wanted to receive SMS related some of the promotions that I might be interested in. Just then, I got a brand new android phone, from my salary and thus opened myself to the fun way of solving this problem myself.
## Solution:
Android being an open source mobile operating system, it helped me achieve the solution of the curious problem I was facing on a day-to-day basis. The approach was so simple to have a Listener or a BroadcastReciever, in android terms, to listen to incoming calls. This being a listener that listens to the event of listening incoming calls. I saved all the patterns and whole phone numbers in the SQLite database so that it would be easy to keep track of patterns and numbers in a more manageable way.
## Sequence:
![Sequence](https://file.ac/3gWa2d-T0JI/image0.png)
## Challenges:
The flow as a whole is simple. The major problems were around the way I store the numbers. As mobile networks send mobile numbers in varied formats, such as +91XXX.., 0XXX.., XXX.. etc., I need to pre process the number as string before saving and after retrieving the number from the database. The next challenge was device specific. For a short period of time, I was using another android phone which was having trouble giving access to the calls that were received, the device hindered the cancelling or ending of calls. That was a problem specific to that particular model of the mobile phone, in order to fix it there was an alternative to turn the airplane mode on and off immediately which will be blocking the call as is the network is unavailable. Even though it was not a clean fix, it was really interesting to poke around with the setting and modes that are related to telephony(mobile networks).
## Views:
- It is a single view app which had minimal functionalities tied to the view.
![Home](https://file.ac/3gWa2d-T0JI/image1.png)

 - Even though there are two sections displaying patterns and numbers separately, internally they were all stored in a single table.
![Add](https://file.ac/3gWa2d-T0JI/image2.png)

 - You can easily lookup from your call logs to block the number from which you have received the call recently.
![CallLogs](https://file.ac/3gWa2d-T0JI/image3.png)

 - In order to use the space in the view I’ve included the remove functionality.
![Remove](https://file.ac/3gWa2d-T0JI/image4.png)

## References:

- Call blocking: http://stackoverflow.com/questions/15012082/rejecting-incoming-call-in-android
- Reading call logs: http://www.compiletimeerror.com/2014/08/android-code-to-read-call-history-with.html#.V7abdJN97q0
- Auto complete text view: https://developer.android.com/reference/android/widget/AutoCompleteTextView.html
