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
* Sending transfer through Ethereum network to peer's account
* Basic encryption for security
* Account session

Todo:
* Fix up interface(especially avoid repeating transfer on screen rotation)
* Put more testing
* Avoid lag when sending transfer
* Make it easier to input ethereum credentials (currently a huge pain and uncertain you'll type it correctly)
* Allow users to modify app account and ethereum accounts more
* Create a logout
* Send push notifications when receiving transfer over the Ethereum network
* Determine transfer fees when preparing transfer

Wishlist:
* Transfers in CAD
* Implement light client to avoid Infura dependency (contingent on Ethereum light clients)
* Lower transfer costs after PoS implemented