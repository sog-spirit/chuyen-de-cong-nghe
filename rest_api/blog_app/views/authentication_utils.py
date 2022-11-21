from django.contrib.auth.models import User
from rest_framework.response import Response
from rest_framework.exceptions import AuthenticationFailed
from rest_framework import status
import jwt

def user_authentication(request):
    token = request.COOKIES.get('jwt')
    if not token:
        raise AuthenticationFailed('User is not authenticated')
    try:
        payload = jwt.decode(token, 'secret', algorithms=['HS256'])
    except jwt.ExpiredSignatureError:
        raise AuthenticationFailed('jwt expired')
    return payload