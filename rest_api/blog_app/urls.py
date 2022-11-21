from django.urls import path
from .views.user_views import UserLogin, UserRegister, UserLogout

urlpatterns = [
    path('user/login', UserLogin.as_view()),
    path('user/register', UserRegister.as_view()),
    path('user/logout', UserLogout.as_view()),
]