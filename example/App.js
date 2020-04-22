/**
 * Sample React Native App
 *
 * adapted from App.js generated by the following command:
 *
 * react-native init example
 *
 * https://github.com/facebook/react-native
 */

import React from 'react';
import {StyleSheet, SafeAreaView, Button} from 'react-native';
import ChatBubbles from 'react-native-chat-bubbles';

const App = () => {
  return (
    <SafeAreaView style={styles.container}>
      <Button
        title="Open Bubble"
        onPress={() => {
          ChatBubbles.open();
        }}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 20,
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default App;
