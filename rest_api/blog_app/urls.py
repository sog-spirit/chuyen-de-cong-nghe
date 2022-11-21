from django.urls import path
from .views.user_views import UserLogin, UserRegister, UserLogout
from .views.post_views import PostAPIView, UserPostsAPIView

urlpatterns = [
    path('user/login', UserLogin.as_view()),
    path('user/register', UserRegister.as_view()),
    path('user/logout', UserLogout.as_view()),
    path('post', PostAPIView.as_view()),
    path('posts', UserPostsAPIView.as_view()),
]