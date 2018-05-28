# Simple-Bitcoin-Wallet
It uses ```bitcoinj``` to implement a simple and easy to use Android wallet.

It will find the other peers, and then sync the blockchain. It only records the related transaction of wallet. It is not likes the full node, so it does not cost too many storage. The users only can see the address, fetched transactions and export the wallet to mnemonic before sync is completed.

## Features
* create HD wallet with 12 words mnemonic
* restore HD wallet from 12 words mnemonic
* export HD wallet to 12 words mnemonic
* display current address
* change address
* receive bitcoin
* send bitcoin
* increase the fee of pending transaction
* list the relevant transactions

## Where to get TestNet Bitcoin
https://testnet.coinfaucet.eu/en

## Bitcoin White paper
https://bitcoincore.org/bitcoin.pdf

## Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/HSY229/SimpleBitcoinWallet/issues).

## Dependencies
* [bitcoinj](https://bitcoinj.github.io/)
* [RxJava2](https://github.com/ReactiveX/RxJava) and RxAndroid
* [Mockito](http://site.mockito.org/)
* [Assertj](http://joel-costigliola.github.io/assertj/)

## Acknowledgements
Thanks to [Bitcoin Wallet for Android](https://github.com/bitcoin-wallet/bitcoin-wallet). We can learn how to use ```bitconj``` library from that highly accomplished project. 

### License

```
Copyright 2018-present, SimpleBitcoinWallet Contributors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

