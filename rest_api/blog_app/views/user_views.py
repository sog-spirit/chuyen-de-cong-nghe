from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from datetime import datetime, timedelta
import jwt
from django.contrib.auth.models import User
from .authentication_utils import user_authentication
from ..serializers import UserSerializer

class UserInfo(APIView):
    def get(self, request):
        payload = user_authentication(request)
        user = User.objects.get(id=payload['id'])
        serializer = UserSerializer(user)
        return Response(serializer.data)

class UserRegister(APIView):
    def post(self, request):
        username = request.data.get('username', None)
        password = request.data.get('password', None)
        if username is None or password is None:
            response = Response()
            message = {}
            if username is None:
                message['username'] = 'This field is required'
            if password is None:
                message['password'] = 'This field is required'
            response.data = message
            response.status_code = status.HTTP_400_BAD_REQUEST
            return response

        user = User.objects.filter(username=username).first()
        if user is not None:
            return Response(
                {'detail': 'Username is existed'},
                status=status.HTTP_400_BAD_REQUEST
            )
        User.objects.create_user(
            username=username,
            password=password
        )
        return Response(
            {'detail': 'User registered successfully'},
            status=status.HTTP_201_CREATED
        )

class UserLogin(APIView):
    def post(self, request):
        username = request.data.get('username', None)
        password = request.data.get('password', None)
        if username is None or password is None:
            response = Response()
            message = {}
            if username is None:
                message['username'] = 'This field is required'
            if password is None:
                message['password'] = 'This field is required'
            response.data = message
            response.status_code = status.HTTP_400_BAD_REQUEST
            return response

        user = User.objects.filter(username=username).first()
        if user is None:
            return Response(
                {'detail': 'User or password is invalid'},
                status=status.HTTP_401_UNAUTHORIZED
            )

        if not user.check_password(password):
            return Response(
                {'detail': 'User or password is invalid'},
                status=status.HTTP_401_UNAUTHORIZED
            )

        payload = {
            'id': user.id,
            'exp': datetime.utcnow() + timedelta(minutes=60),
            'iat': datetime.utcnow()
        }
        token = jwt.encode(payload, 'secret', algorithm='HS256')
        response = Response()
        response.set_cookie(key='jwt', value=token)
        response.data = {
            'detail': 'Login successfully',
            'jwt': token
        }
        return response

class UserLogout(APIView):
    def post(self, request):
        response = Response()
        response.delete_cookie('jwt')
        response.data = {
            'detail': 'Logout successfully'
        }
        return response