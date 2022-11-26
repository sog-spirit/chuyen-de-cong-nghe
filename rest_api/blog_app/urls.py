from django.urls import path
from .views.user_views import UserLogin, UserRegister, UserLogout, UserInfo, UserList
from .views.post_views import PostAPIView, UserPostsAPIView
from .views.comment_views import CommentAPIView, CommentsAPIView
from .views.chat_views import ChatsAPIView, GetChatIdAPIView
from .views.message_views import GetMessageAPIView, GetNewestMessageAPIView, CreateMessageAPIView

urlpatterns = [
    path('users', UserList.as_view()),
    path('user/info', UserInfo.as_view()),
    path('user/login', UserLogin.as_view()),
    path('user/register', UserRegister.as_view()),
    path('user/logout', UserLogout.as_view()),
    path('post', PostAPIView.as_view()),
    path('posts', UserPostsAPIView.as_view()),
    path('comment', CommentAPIView.as_view()),
    path('comments', CommentsAPIView.as_view()),
    path('chat', GetChatIdAPIView.as_view()),
    path('chats', ChatsAPIView.as_view()),
    path('messages/get', GetMessageAPIView.as_view()),
    path('messages/get/newest', GetNewestMessageAPIView.as_view()),
    path('messages/create', CreateMessageAPIView.as_view()),
]