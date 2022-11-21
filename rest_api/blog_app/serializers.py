from .models import Post
from rest_framework import serializers

class PostSerializer(serializers.ModelSerializer):
    name = serializers.CharField(source='user.username', read_only=True)

    class Meta:
        model = Post
        fields = '__all__'