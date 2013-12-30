class Notification < ActiveRecord::Base
  belongs_to :user
  belongs_to :location
  belongs_to :territory
  scope :unread, conditions: {read: false}

  def notification_info
    case self.notification_type
    when "entering"
      self.location.to_hash
    when "detection"
      self.territory.to_hash
    end.merge({user_id: self.user.id, user_name: self.user.name})
  end
end
