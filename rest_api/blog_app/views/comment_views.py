from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from ..serializers import CommentSerializer
from ..models import Post, Comment
from .authentication_utils import user_authentication

class CommentsAPIView(APIView):
    def post(self, request):
        post_id = request.data.get('post_id', None)
        if post_id is None:
            return Response(
                {'detail': 'Post id is empty'},
                status=status.HTTP_400_BAD_REQUEST
            )
        try:
            post = Post.objects.get(id=post_id)
        except Post.DoesNotExist:
            return Response(
                {'detail': 'Post not found'},
                status=status.HTTP_404_NOT_FOUND
            )
        comments = post.comments.filter(post=post_id)
        serializer = CommentSerializer(comments, many=True)
        return Response(serializer.data)