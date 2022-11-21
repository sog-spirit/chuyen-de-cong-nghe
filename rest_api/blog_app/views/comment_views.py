from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from ..serializers import CommentSerializer
from ..models import Post, Comment
from .authentication_utils import user_authentication

class CommentAPIView(APIView):
    def get(self, request):
        comment_id = request.GET.get('comment_id', None)
        if comment_id is None:
            return Response(
                {'detail': 'Comment id is empty'},
                status=status.HTTP_400_BAD_REQUEST
            )
        try:
            comment = Comment.objects.get(id=comment_id)
        except Comment.DoesNotExist:
            return Response(
                {'detail': 'Comment not found'},
                status=status.HTTP_400_BAD_REQUEST
            )
        serializer = CommentSerializer(comment)
        return Response(serializer.data)

    def post(self, request):
        payload = user_authentication(request)
        data = request.data.copy()
        data['user'] = payload['id']
        serializer = CommentSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(
                {'detail': 'Comment created successfully'},
                status=status.HTTP_200_OK
            )
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def patch(self, request):
        payload = user_authentication(request)
        comment_id = request.data.get('comment_id', None)
        if comment_id is None:
            return Response(
                {'detail': 'Comment id is empty'},
                status=status.HTTP_400_BAD_REQUEST
            )
        try:
            comment = Comment.objects.get(id=comment_id)
        except Comment.DoesNotExist:
            return Response(
                {'detail': 'Comment not found'},
                status=status.HTTP_400_BAD_REQUEST
            )
        if comment.user.id != payload['id']:
            return Response(
                {'detail': 'Not this user comment'},
                status=status.HTTP_400_BAD_REQUEST
            )
        serializer = CommentSerializer(instance=comment, data=request.data, partial=True)
        if serializer.is_valid():
            serializer.save()
            return Response(
                {'detail': 'Edit comment successfully'},
                status=status.HTTP_200_OK
            )
        return Response(
            serializer.errors,
            status=status.HTTP_400_BAD_REQUEST
        )

    def delete(self, request):
        payload = user_authentication(request)
        comment_id = request.data.get('comment_id', None)
        if comment_id is None:
            return Response(
                {'detail': 'Comment id is empty'},
                status=status.HTTP_400_BAD_REQUEST
            )
        try:
            comment = Comment.objects.get(id=comment_id)
        except Comment.DoesNotExist:
            return Response(
                {'detail': 'Comment not found'},
                status=status.HTTP_400_BAD_REQUEST
            )
        if comment.user.id != payload['id']:
            return Response(
                {'detail': 'Not this user comment'},
                status=status.HTTP_400_BAD_REQUEST
            )
        comment.delete()
        return Response(
            {'detail': 'Comment deleted successfully'},
            status=status.HTTP_200_OK
        )

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