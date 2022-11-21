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

    def post(self, request):
        payload = user_authentication(request)
        data = request.data.copy()
        data['user'] = payload['id']
        serializer = PostSerializer(data=data)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(
                {'detail': 'Post created successfully'},
                status=status.HTTP_201_CREATED
            )
        return Response(
            serializer.errors,
            status=status.HTTP_400_BAD_REQUEST
        )

class UserPostsAPIView(APIView):
    def get(self, request):
        """
        Get all current user posts
        """
        payload = user_authentication(request)
        posts = Post.objects.filter(user=payload['id'])
        serializer = PostSerializer(posts, many=True)
        return Response(serializer.data)