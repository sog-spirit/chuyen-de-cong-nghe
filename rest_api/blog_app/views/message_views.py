from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from ..serializers import MessageSerializer
from ..models import Chat, Message
from .authentication_utils import user_authentication
from django.db.models import Q

class GetMessageAPIView(APIView):
    def post(self, request):
        payload = user_authentication(request)
        user_two_id = request.data.get('user_two', None)
        if user_two_id is None:
            return Response(
                {'detail': 'user_two is empty'},
                status=status.HTTP_400_BAD_REQUEST
            )
        if user_two_id == payload['id']:
            return Response(
                {'detail': 'Duplicate with current user id'},
                status=status.HTTP_400_BAD_REQUEST
            )
        try:
            chat = Chat.objects.get(
                (Q(user_one_id=payload['id']) | Q(user_two_id=payload['id'])) &
                (Q(user_one_id=user_two_id) | Q(user_two_id=user_two_id))
            )
            messages = Message.objects.filter(chat=chat)
            serializer = MessageSerializer(messages, many=True)
            return Response(serializer.data)

        except Chat.DoesNotExist:
            return Response(
                {'detail': 'Chat does not exist'},
                status=status.HTTP_404_NOT_FOUND
            )

class GetNewestMessageAPIView(APIView):
    def post(self, request):
        payload = user_authentication(request)
        user_two_id = request.data.get('user_two', None)
        if user_two_id is None:
            return Response(
                {'detail': 'user_two is empty'},
                status=status.HTTP_400_BAD_REQUEST
            )
        if user_two_id == payload['id']:
            return Response(
                {'detail': 'Duplicate with current user id'},
                status=status.HTTP_400_BAD_REQUEST
            )
        try:
            chat = Chat.objects.get(
                (Q(user_one_id=payload['id']) | Q(user_two_id=payload['id'])) &
                (Q(user_one_id=user_two_id) | Q(user_two_id=user_two_id))
            )
            message = Message.objects.filter(chat=chat).last()
            serializer = MessageSerializer(message)
            return Response(serializer.data)

        except Chat.DoesNotExist:
            return Response(
                {'detail': 'Chat does not exist'},
                status=status.HTTP_404_NOT_FOUND
            )

class CreateMessageAPIView(APIView):
    def post(self, request):
        payload = user_authentication(request)
        data = request.data.copy()
        serializer = MessageSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(
                {'detail': 'Message created successfully'},
                status=status.HTTP_200_OK
            )
        return Response(
            serializer.errors,
            status=status.HTTP_400_BAD_REQUEST
        )