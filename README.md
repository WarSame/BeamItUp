# BeamItUp
Peer to peer ethereum transfers on Android.

Working:
* Login/create app account
* Create ethereum account
* Retrieve associated ethereum account for app account
* Create transfer
* Ready transfer for sending with NFC
* Receiving transfer with NFC and preparing reply
* Receiving reply
* Sending transfer through Ethereum network to peer's account received in reply
* Basic encryption for security
* Account session

Todo:
* Fix up interface(especially avoid repeating transfer on screen rotation)
* More testing
* Avoid lag when sending transfer

Wishlist:
* Transfers in CAD
* Implement light client to avoid Infura dependency (contingent on Ethereum light clients)
* Announce likely transfer fees when preparing transfer
