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

    def post(self, request):
        payload = user_authentication(request)
        post_id = request.data.get('post_id', None)
        if post_id is None:
            return Response(
                {'detail': 'post_id is required'},
                status=status.HTTP_400_BAD_REQUEST
            )
        try:
            post = Post.objects.get(id=post_id)
        except Post.DoesNotExist:
            return Response(
                {'detail': 'Post not found'},
                status=status.HTTP_400_BAD_REQUEST
            )
        if post.user.id != payload['id']:
            return Response(
                {'detail': 'Not current user post'},
                status=status.HTTP_403_FORBIDDEN
            )
        serializer = PostSerializer(post)
        return Response(serializer.data)

    def patch(self, request):
        payload = user_authentication(request)
        post_id = request.data.get('post_id', None)
        if post_id is None:
            return Response(
                {'detail': 'post_id is required'},
                status=status.HTTP_400_BAD_REQUEST
            )
        try:
            post = Post.objects.get(id=post_id)
        except Post.DoesNotExist:
            return Response(
                {'detail': 'Post not found'},
                status=status.HTTP_400_BAD_REQUEST
            )
        if post.user.id != payload['id']:
            return Response(
                {'detail': 'Not current user post'},
                status=status.HTTP_403_FORBIDDEN
            )
        serializer = PostSerializer(instance=post, data=request.data, partial=True)
        if serializer.is_valid():
            serializer.save()
            return Response(
                {'detail': 'Post edit successfully'},
                status=status.HTTP_200_OK
            )
        return Response(
            serializer.errors,
            status=status.HTTP_400_BAD_REQUEST
        )