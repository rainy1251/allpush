package com.myapp.allpush;

import java.util.List;

public class MsgBean {

    private MessageBean message;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        private NotificationBean notification;
        private AndroidBean android;
        private List<String> token;

        public NotificationBean getNotification() {
            return notification;
        }

        public void setNotification(NotificationBean notification) {
            this.notification = notification;
        }

        public AndroidBean getAndroid() {
            return android;
        }

        public void setAndroid(AndroidBean android) {
            this.android = android;
        }

        public List<String> getToken() {
            return token;
        }

        public void setToken(List<String> token) {
            this.token = token;
        }

        public static class NotificationBean {
            private String title;
            private String body;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }
        }

        public static class AndroidBean {
            private NotificationBeanX notification;

            public NotificationBeanX getNotification() {
                return notification;
            }

            public void setNotification(NotificationBeanX notification) {
                this.notification = notification;
            }

            public static class NotificationBeanX {
                private String icon;
                private String importance;
                private String channel_id;
                private ClickActionBean click_action;

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getImportance() {
                    return importance;
                }

                public void setImportance(String importance) {
                    this.importance = importance;
                }

                public String getChannel_id() {
                    return channel_id;
                }

                public void setChannel_id(String channel_id) {
                    this.channel_id = channel_id;
                }

                public ClickActionBean getClick_action() {
                    return click_action;
                }

                public void setClick_action(ClickActionBean click_action) {
                    this.click_action = click_action;
                }

                public static class ClickActionBean {
                    private int type;
                    private String url;

                    public int getType() {
                        return type;
                    }

                    public void setType(int type) {
                        this.type = type;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }
            }
        }
    }
}
