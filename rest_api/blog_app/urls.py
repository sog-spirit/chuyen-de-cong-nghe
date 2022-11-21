from django.urls import path
from .views.user_views import UserLogin

urlpatterns = [
    path('user/login', UserLogin.as_view()),
]