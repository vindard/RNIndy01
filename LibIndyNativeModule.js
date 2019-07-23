//  Created by react-native-create-bridge

import { NativeModules } from 'react-native'

const { LibIndy } = NativeModules

export default {
  async exampleMethod () {
    console.log("Ran JS wrapper!");
    const api = await LibIndy.init();
    console.log("Loaded Libindy library (LibIndy.init)");
    const text = await LibIndy.exampleMethod();
    console.log("Ran example method from Java:", text);
    console.log("Running async Libindy.createWallet() method...");
    const wallet = await LibIndy.createWallet();
    console.log("Wallet handle: ", wallet);
    console.log("Ran createWallet()!");
  },

  EXAMPLE_CONSTANT: LibIndy.EXAMPLE_CONSTANT
}
