import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: "WSDebug",
  data() {
    return {
      received_messages: [],
      send_message: null,
      connected: false
    };
  },
  methods: {
    send() {
      console.log("Send message:" + this.send_message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = this.send_message
        this.stompClient.send("/app/message/send", msg, {});
      }
    },
    connect() {
      this.socket = new SockJS(process.env.WEBSOCKET_SERVER_URL);
      this.stompClient = Stomp.over(this.socket);
      this.stompClient.connect(
        {},
        frame => {
          this.connected = true;
          //console.log(frame);
          ///topic/activity/model/card/{cardWrapperId}/page/{page}/size/{size}/{onlyMessages}
          ///topic/activity/model/card/c0a80f4b-628a-1d31-8162-8ad0f0d5000f
          this.stompClient.subscribe("/channel/chat", tick => {
            
            //var res = JSON.parse(tick.body);
            console.log(tick.body);
            console.log('sagar')
            //this.received_messages.push(tick.body);
          });
        },
        error => {
          console.log(error);
          this.connected = false;
        }
      );
    },
    disconnect() {
      if (this.stompClient) {
        this.stompClient.disconnect();
      }
      this.connected = false;
    },
    tickleConnection() {
      this.connected ? this.disconnect() : this.connect();
    }
  },
  mounted() {
    // this.connect();
  }
};