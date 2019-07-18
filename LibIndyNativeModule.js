//  Created by react-native-create-bridge

import { NativeModules } from 'react-native'

const { LibIndy } = NativeModules

export default {
  async exampleMethod () {
    console.log("Ran JS wrapperzzz!");
    const api = await LibIndy.init();
    console.log("Bye there1");
    const text = await LibIndy.exampleMethod();
    console.log("Bye there2");
    const wallet = await LibIndy.createWallet();
    console.log(wallet)
    console.log("Bye there3");
  },

  EXAMPLE_CONSTANT: LibIndy.EXAMPLE_CONSTANT
}
