from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from ..serializers import ChatSerializer
from ..models import Chat
from .authentication_utils import user_authentication
from django.db.models import Q

class ChatsAPIView(APIView):
    def get(self, request):
        payload = user_authentication(request)
        chats = Chat.objects.filter(
            Q(user_one_id=payload['id']) | Q(user_two_id=payload['id'])
        )
        serializer = ChatSerializer(chats, many=True)
        return Response(serializer.data)

    def post(self, request):
        payload = user_authentication(request)
        user_two_id = request.data.get('user_two', None)
        if user_two_id is None:
            return Response(
                {'user_two': 'This field is required'},
                status=status.HTTP_404_NOT_FOUND
            )
        try:
            chat = Chat.objects.get(
                (Q(user_one_id=payload['id']) | Q(user_two_id=payload['id'])) &
                (Q(user_one_id=user_two_id) | Q(user_two_id=user_two_id))
            )
            return Response(
                {'detail': 'Chat is existed'},
                status=status.HTTP_302_FOUND
            )
        except Chat.DoesNotExist:
            chat = Chat.objects.create(
                user_one_id=payload['id'],
                user_two_id=user_two_id
            )
            return Response(
                {'id': chat.id},
                status=status.HTTP_200_OK
            )

class GetChatIdAPIView(APIView):
    def post(self, request):
        payload = user_authentication(request)
        user_two_id = request.data.get('user_two', None)
        if user_two_id is None:
            return Response(
                {'detail': 'user_two not found'},
                status=status.HTTP_400_BAD_REQUEST
            )
        try:
            chat = Chat.objects.get(
                (Q(user_one_id=payload['id']) | Q(user_two_id=payload['id'])) &
                (Q(user_one_id=user_two_id) | Q(user_two_id=user_two_id))
            )
            return Response(
                {'id': chat.id},
                status=status.HTTP_200_OK
            )
        except Chat.DoesNotExist:
            return Response(
                {'detail': 'Chat not found'},
                status=status.HTTP_404_NOT_FOUND
            )