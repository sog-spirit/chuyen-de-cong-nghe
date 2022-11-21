from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from ..serializers import PostSerializer
from ..models import Post
from .authentication_utils import user_authentication

class PostAPIView(APIView):
    def get(self, request):
        posts = Post.objects.filter(status="PUBLISH")
        serializer = PostSerializer(posts, many=True)
        return Response(serializer.data)