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
        this.stompClient.send("/app/messages/send", msg, {});
      }
    },
    connect() {
      this.socket = new SockJS(process.env.WEBSOCKET_SERVER_URL);
      this.stompClient = Stomp.over(this.socket);
      this.stompClient.connect(
        {},
        frame => {
          this.connected = true;
          console.log(frame);
          this.stompClient.subscribe("/topic/conversation", tick => {
            console.log(tick.body);
            this.received_messages.push(tick.body);
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