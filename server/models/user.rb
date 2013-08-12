require 'securerandom'

class User < ActiveRecord::Base
  belongs_to :room
  before_create :generate_token

  def generate_token
    token = SecureRandom.base64(16)
    self.token = token
  end

  def enter_room(room)
    self.room = room
    self.save
  end
end
